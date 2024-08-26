import EastIcon from "@mui/icons-material/East";
import { Box } from "@mui/material";
import Grid from "@mui/material/Grid";
import { useTranslation } from "react-i18next";
import { TextVariant } from "../../constants/Const";
import { Article } from "../../requests/articleList";
import { color } from "../../styles/colors";
import { GridElement } from "../atoms/ArticleGridElement";
import { Heading } from "../atoms/Heading";
import { NavLink } from "../atoms/Link";
import { RoutePath } from "../../router/RoutePath ";

interface SideArticlesProps {
  articles: Article[];
}

export const TrilerOfArticles = ({ articles }: SideArticlesProps) => {
  const { t } = useTranslation();

  if (articles.length === 0) {
    return (
      <>
        <Box
          sx={{
            display: "flex",
            flexDirection: { xs: "column", md: "column", lg: "row", xl: "row" },
            height: "auto",
            justifyContent: "space-between",
            alignItems: "center",
            paddingLeft: "10px",
          }}
        >
          <Box
            sx={{
              display: "flex",
              justifyContent: "start",
              width: { xs: "100%", md: "50%", lg: "50%" },
            }}
          >
            <Heading variant={TextVariant.H5} sx={{ fontWeight: "bold" }}>
              {t("trilerOfArticle.latestArticles.label")}
            </Heading>
          </Box>
          <Box
            sx={{
              display: "flex",
              justifyContent: "end",
              width: { xs: "100%", md: "50%", lg: "50%" },
            }}
          >
            <NavLink color={color.textSecondary} to={RoutePath.ALL_ARTICLES}>
              <Box sx={{ display: "flex", alignItems: "center" }}>
                <EastIcon sx={{ paddingRight: "5px" }} />

                <Heading
                  variant={TextVariant.H6}
                  sx={{
                    fontWeight: "bold",
                    padding: "0px",
                    color: color.textSecondary,
                  }}
                >
                  {t("trilerOfArticle.viewAll.label")}
                </Heading>
              </Box>
            </NavLink>
          </Box>
        </Box>

        <Box sx={{ color: color.white, padding: "20px" }}>
          <Heading
            variant={TextVariant.H6}
            sx={{ color: color.textPrimary, fontSize: "32px" }}
          >
            {t("startArticles.noArticle.label")}
          </Heading>
        </Box>
      </>
    );
  }

  return (
    <Box>
      <Box
        sx={{
          display: "flex",
          flexDirection: { xs: "column", md: "column", lg: "row", xl: "row" },
          height: "auto",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <Box
          sx={{
            display: "flex",
            justifyContent: "start",
            width: { xs: "100%", md: "50%", lg: "50%" },
          }}
        >
          <Heading variant={TextVariant.H5} sx={{ fontWeight: "bold" }}>
            {t("trilerOfArticle.latestArticles.label")}
          </Heading>
        </Box>
        <Box
          sx={{
            display: "flex",
            justifyContent: "end",
            width: { xs: "100%", md: "50%", lg: "50%" },
          }}
        >
          <NavLink color={color.textSecondary} to={RoutePath.ALL_ARTICLES}>
            <Box sx={{ display: "flex", alignItems: "center" }}>
              <EastIcon sx={{ paddingRight: "5px" }} />

              <Heading
                variant={TextVariant.H6}
                sx={{
                  fontWeight: "bold",
                  padding: "0px",
                  color: color.textSecondary,
                }}
              >
                {t("trilerOfArticle.viewAll.label")}
              </Heading>
            </Box>
          </NavLink>
        </Box>
      </Box>

      <Grid container wrap="wrap" justifyContent="center" gap="50px">
        {articles.map((item, index) => (
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
    </Box>
  );
};
