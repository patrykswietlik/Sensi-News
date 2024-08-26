import { useState } from "react";
import { Box } from "@mui/material";
import Grid from "@mui/material/Grid";
import { RoutePath } from "../router/RoutePath ";
import { GridElement } from "../components/atoms/ArticleGridElement";
import { NavLink } from "../components/atoms/Link";
import { ScaffoldTemplate } from "../components/templates/ScaffoldTemplate";
import { color } from "../styles/colors";
import Pagination from "@mui/material/Pagination";
import { useTranslation } from "react-i18next";
import { Heading } from "../components/atoms/Heading";
import { TextVariant } from "../constants/Const";
import { useEffect } from "react";
import { Article, getAllArticles } from "../requests/articleList";
import { LoadingSpinner } from "../components/atoms/LoadingSpinner";

export const AllArticlesPage = () => {
  const [content, setContent] = useState<Article[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const fetchContent = async () => {
    setIsLoading(true);
    const content = await getAllArticles();
    setContent(content);
    setIsLoading(false);
  };

  useEffect(() => {
    fetchContent();
  }, []);

  const [page, setPage] = useState(0);
  const ROWS_PER_PAGE = 7;
  const paginatedData = content.slice(
    page * ROWS_PER_PAGE,
    page * ROWS_PER_PAGE + ROWS_PER_PAGE,
  );
  const pageCount = Math.ceil(content.length / ROWS_PER_PAGE);
  const { t } = useTranslation();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);

  if (isLoading) {
    return <LoadingSpinner />;
  }

  if (content.length === 0) {
    return (
      <ScaffoldTemplate>
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            backgroundColor: color.gray25,
            padding: "20px",
            minHeight: "80vh",
            height: "auto",
          }}
        >
          <Heading
            variant={TextVariant.H6}
            sx={{
              paddingLeft: "20px",
              color: color.textPrimary,
              fontSize: "32px",
            }}
          >
            {t("startArticles.noArticle.label")}
          </Heading>
        </Box>
      </ScaffoldTemplate>
    );
  }

  return (
    <ScaffoldTemplate>
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          backgroundColor: color.gray25,
          minHeight: "80vh",
          height: "auto",
        }}
      >
        <Box sx={{ width: { xs: "0%", md: "5%", lg: "10%", xl: "20%" } }} />
        <Box display="flex" flexDirection="column">
          <Heading
            variant={TextVariant.H6}
            sx={{ paddingTop: "20px", paddingLeft: "10px", fontSize: "32px" }}
          >
            {" "}
            {t("articleContent.allArticles.label")}{" "}
          </Heading>
          <Grid container wrap="wrap" justifyContent="start" gap="50px">
            {paginatedData.map((item, index) => (
              <Box key={index}>
                <NavLink to={RoutePath.ARTICLE.replaceAll(":id", item.id)}>
                  <GridElement
                    id={item.id}
                    title={item.title}
                    image={item.image}
                    userAccountUsername={item.userAccountUsername}
                    content={item.content}
                    createdAt={item.createdAt}
                    tagsIds={item.tagsIds}
                    userAccountId={item.userAccountId}
                    updatedAt={item.updatedAt}
                    views={item.views}
                    likesCount={item.likesCount}
                  />
                </NavLink>
              </Box>
            ))}
          </Grid>
          <Pagination
            count={pageCount}
            page={page + 1}
            onChange={(event, value) => setPage(value - 1)}
            variant="outlined"
            shape="rounded"
            siblingCount={0}
            boundaryCount={1}
            sx={{
              width: "100%",
              display: "flex",
              justifyContent: "center",
              marginBottom: "20px",
            }}
          />
        </Box>
        <Box sx={{ width: { xs: "0%", md: "5%", lg: "10%", xl: "20%" } }} />
      </Box>
    </ScaffoldTemplate>
  );
};
