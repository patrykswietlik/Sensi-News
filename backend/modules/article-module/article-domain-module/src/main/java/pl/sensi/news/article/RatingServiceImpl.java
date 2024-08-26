package pl.sensi.news.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sensi.news.article.api.RatingService;
import pl.sensi.news.article.api.models.Rating;
import pl.sensi.news.security.SecurityUtils;

@Service
@RequiredArgsConstructor
class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Override
    public int getCountByArticleId(String id) {
        return ratingRepository.getCountByArticleId(id);
    }

    @Override
    public Rating rate(String id) {
        String loggedInUserId = SecurityUtils.getLoggedInUserId();

        if(ratingRepository.existsByIds(id, loggedInUserId)) {
            ratingRepository.deleteByArticleId(id);
            return null;
        }

        return ratingRepository.save(Rating.builder()
                .articleId(id)
                .userAccountId(loggedInUserId)
                .build());
    }
}
