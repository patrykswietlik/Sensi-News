package pl.sensi.news.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.sensi.news.article.api.models.View;

@Repository
@RequiredArgsConstructor
class ViewRepositoryImpl implements ViewRepository {

    private final ViewRepositoryJpa viewRepositoryJpa;

    private final ViewMapperJpa viewMapperJpa;

    @Override
    public void save(View view) {
        viewRepositoryJpa.save(viewMapperJpa.toEntity(view));
    }

    @Override
    public boolean existsByArticleIdAndIpAddress(String articleId, String ip) {
        return viewRepositoryJpa.existsByArticleIdAndUserIpAddress(articleId, ip);
    }
}
