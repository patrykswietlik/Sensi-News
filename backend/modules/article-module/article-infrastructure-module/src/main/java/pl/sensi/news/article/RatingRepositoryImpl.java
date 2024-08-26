package pl.sensi.news.article;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.sensi.news.article.api.models.Rating;

@Repository
@RequiredArgsConstructor
class RatingRepositoryImpl implements RatingRepository {

    private final RatingRepositoryJpa ratingRepositoryJpa;

    private final RatingMapperJpa ratingMapperJpa;

    @Override
    public int getCountByArticleId(String id) {
        return ratingRepositoryJpa.countByArticleId(id);
    }

    @Override
    public Rating save(Rating rating) {
        RatingEntity createdRating = ratingRepositoryJpa.save(ratingMapperJpa.toEntity(rating));
        return ratingMapperJpa.toDomain(createdRating);
    }

    @Override
    @Transactional
    public void deleteByArticleId(String id) {
        ratingRepositoryJpa.deleteByArticleId(id);
    }

    @Override
    public boolean existsByIds(String articleId, String userAccountId) {
        return ratingRepositoryJpa.existsByArticleIdAndUserAccountId(articleId, userAccountId);
    }
}
