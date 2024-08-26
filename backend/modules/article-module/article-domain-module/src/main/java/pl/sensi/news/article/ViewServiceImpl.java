package pl.sensi.news.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sensi.news.article.api.ViewService;
import pl.sensi.news.article.api.models.View;
import pl.sensi.news.security.SecurityUtils;

@Service
@RequiredArgsConstructor
class ViewServiceImpl implements ViewService {

    private final ViewRepository viewRepository;

    @Override
    public boolean handleView(String id) {
        String currentUserIp = SecurityUtils.getCurrentUserIp();

        if (!viewRepository.existsByArticleIdAndIpAddress(id, currentUserIp)) {
            viewRepository.save(
                    View.builder()
                        .articleId(id)
                        .userIpAddress(currentUserIp)
                        .build()
            );

           return true;
        }

        return false;
    }
}
