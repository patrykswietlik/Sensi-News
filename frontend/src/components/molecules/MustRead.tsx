import { Box } from "@mui/material";
import Grid from "@mui/material/Grid";
import { useTheme } from "@mui/material/styles";
import useMediaQuery from "@mui/material/useMediaQuery";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { TextVariant } from "../../constants/Const";
import { Article, getAllArticles } from "../../requests/articleList";
import { RoutePath } from "../../router/RoutePath ";
import { GridElement } from "../atoms/ArticleGridElement";
import { Heading } from "../atoms/Heading";
import { NavLink } from "../atoms/Link";

export const MustRead = () => {
  const { t } = useTranslation();
  const theme = useTheme();
  const sm = useMediaQuery(theme.breakpoints.down("sm"));
  const md = useMediaQuery(theme.breakpoints.down("md"));
  const [content, setContent] = useState<Article[]>([]);

  const fetchContent = async () => {
    const content = await getAllArticles();
    setContent(content);
  };

  useEffect(() => {
    fetchContent();
  }, []);

  const twoElements = content.slice(1, 3);
  return (
    <Box>
      <Heading
        variant={TextVariant.H5}
        sx={{
          fontWeight: "bold",
          paddingTop: "20px",
          paddingRight: "0px",
        }}
      >
        {t("articleContent.mustRead.label")}
      </Heading>

      <Grid
        container
        wrap="wrap"
        justifyContent={md ? "center" : "start"}
        gap="50px"
      >
        {twoElements.map((item, index) => (
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
                widthImg={sm ? "270px" : "370px"}
              />
            </NavLink>
          </Box>
        ))}
      </Grid>
    </Box>
  );
};
