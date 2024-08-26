import { Box } from "@mui/material";
import Grid from "@mui/material/Grid";
import Pagination from "@mui/material/Pagination";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { GridElement } from "../components/atoms/ArticleGridElement";
import { Heading } from "../components/atoms/Heading";
import { NavLink } from "../components/atoms/Link";
import { LoadingSpinner } from "../components/atoms/LoadingSpinner";
import { ScaffoldTemplate } from "../components/templates/ScaffoldTemplate";
import { Language, TextVariant } from "../constants/Const";
import { Article, getArticlesByTag } from "../requests/articleList";
import { getTagById, Tag } from "../requests/tags";
import { RoutePath } from "../router/RoutePath ";
import { color } from "../styles/colors";

export const AllArticlesInTagPage = () => {
  const [isLoading, setIsLoading] = useState(true);
  const [tag, setTag] = useState<Tag>();
  const [filteredData, setfilteredData] = useState<Article[]>([]);
  const { category = "" } = useParams();
  const navigate = useNavigate();

  const fetchTag = async () => {
    const tag = await getTagById(category);
    setTag(tag);
  };

  useEffect(() => {
    fetchTag();
  }, [category]);

  const fetchFilteredData = async () => {
    setIsLoading(true);
    const filteredData = await getArticlesByTag(category);
    if (filteredData.length !== 0) {
      setfilteredData(filteredData);
    } else {
      navigate(RoutePath.START);
    }
    setIsLoading(false);
  };

  useEffect(() => {
    fetchFilteredData();
  }, [category]);

  const [page, setPage] = useState(0);
  const ROWS_PER_PAGE = 4;
  const paginatedData = filteredData.slice(
    page * ROWS_PER_PAGE,
    page * ROWS_PER_PAGE + ROWS_PER_PAGE,
  );
  const pageCount = Math.ceil(filteredData.length / ROWS_PER_PAGE);
  const { t, i18n } = useTranslation();
  const displayTagName = tag
    ? i18n.language === Language.PL
      ? tag.plName
      : tag.engName
    : "";

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);

  if (isLoading) {
    return <LoadingSpinner />;
  }

  if (filteredData.length === 0) {
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
              fontSize: "28px",
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
      {filteredData && (
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
              sx={{ paddingTop: "20px", paddingLeft: "10px", fontSize: "28px" }}
            >
              {displayTagName}
            </Heading>
            <Grid container wrap="wrap" justifyContent="center" gap="50px">
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
      )}
    </ScaffoldTemplate>
  );
};
