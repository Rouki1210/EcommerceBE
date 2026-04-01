package com.example.ecommerceBE.Service.Impl;

import com.example.ecommerceBE.Config.JwtUtil;
import com.example.ecommerceBE.exception.AppException;
import com.example.ecommerceBE.exception.ErrorCode;
import com.example.ecommerceBE.Dtos.CartItemRequest;
import com.example.ecommerceBE.Dtos.CartResponse;
import com.example.ecommerceBE.Dtos.UpdateCartItemRequest;
import com.example.ecommerceBE.mapper.CartMapper;
import com.example.ecommerceBE.Repository.CartItemRepository;
import com.example.ecommerceBE.Repository.CartRepository;
import com.example.ecommerceBE.Repository.ProductRepository;
import com.example.ecommerceBE.Repository.UserRepository;
import com.example.ecommerceBE.Service.Interface.CartService;
import com.example.ecommerceBE.entity.Cart;
import com.example.ecommerceBE.entity.CartItem;
import com.example.ecommerceBE.entity.Product;
import com.example.ecommerceBE.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final CartMapper cartMapper;

    private User extractUser(String authHeader) {
        String email = jwtUtil.extractEmail(authHeader.substring(7));
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    @Override
    public CartResponse getMyCart(String authHeader) {
        User user = extractUser(authHeader);
        Cart cart = getOrCreateCart(user);
        return cartMapper.toCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addToCart(String authHeader, CartItemRequest request) {
        User user = extractUser(authHeader);
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (!product.getIsActive()) {
            throw new RuntimeException("Sản phẩm không còn bán");
        }

        if (product.getStock() < request.getQuantity()) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        // Nếu sản phẩm đã có trong cart thì cộng thêm số lượng
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        cartItemRepository.save(cartItem);

        Cart updatedCart = cartRepository.findByUser(user).orElseThrow();
        return cartMapper.toCartResponse(updatedCart);
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(String authHeader, String cartItemId, UpdateCartItemRequest request) {
        User user = extractUser(authHeader);
        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng"));

        // Kiểm tra cartItem có thuộc về cart của user không
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Bạn không có quyền cập nhật sản phẩm này");
        }

        if (cartItem.getProduct().getStock() < request.getQuantity()) {
            throw new RuntimeException("Sản phẩm không đủ số lượng trong kho");
        }

        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);

        Cart updatedCart = cartRepository.findByUser(user).orElseThrow();
        return cartMapper.toCartResponse(updatedCart);
    }

    @Override
    @Transactional
    public CartResponse removeFromCart(String authHeader, String cartItemId) {
        User user = extractUser(authHeader);
        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng"));

        // Kiểm tra cartItem có thuộc về cart của user không
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa sản phẩm này");
        }

        cartItemRepository.delete(cartItem);

        Cart updatedCart = cartRepository.findByUser(user).orElseThrow();
        return cartMapper.toCartResponse(updatedCart);
    }

    @Override
    @Transactional
    public void clearCart(String authHeader) {
        User user = extractUser(authHeader);
        Cart cart = getOrCreateCart(user);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}