package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.dto.ReviewDTO;
import com.example.cryptoExchange.model.MyUserDetails;
import com.example.cryptoExchange.model.Review;
import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.service.unified.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

import static com.example.cryptoExchange.constants.UrlAddress._REVIEWS;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping(_REVIEWS)
    public ModelAndView reviews() {
        return new ModelAndView(_REVIEWS);
    }

    @PostMapping(_REVIEWS)
    public ModelAndView setReviews(ReviewDTO reviewDTO) {

        return new ModelAndView(_REVIEWS);
    }
}
