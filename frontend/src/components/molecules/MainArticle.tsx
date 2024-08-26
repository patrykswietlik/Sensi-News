import { Box } from "@mui/material";
import { styled, useTheme } from "@mui/material/styles";
import useMediaQuery from "@mui/material/useMediaQuery";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Language, TextVariant } from "../../constants/Const";
import { Article } from "../../requests/articleList";
import { getAllTagsByIds, Tag } from "../../requests/tags";
import { RoutePath } from "../../router/RoutePath ";
import { color } from "../../styles/colors";
import { ArticleInfo } from "../atoms/ArticleInfo";
import { Heading } from "../atoms/Heading";
import { NavLink } from "../atoms/Link";
import { TextPreview } from "../atoms/TextPreview";

interface MainArticleProps {
  article?: Article;
}

export const MainArticle = ({ article }: MainArticleProps) => {
  const theme = useTheme();
  const sm = useMediaQuery(theme.breakpoints.down("sm"));
  const { t, i18n } = useTranslation();
  const [tags, setTags] = useState<Tag[]>([]);

  useEffect(() => {
    const fetchTags = async () => {
      if (article?.tagsIds) {
        const tags = await getAllTagsByIds(article.tagsIds);
        setTags(tags);
      }
    };
    fetchTags();
  }, [article]);
  const BoxTheme = styled(Box)(() => ({
    position: "relative",
    backgroundImage: `url(${article?.image || ""})`,
    backgroundRepeat: "no-repeat",
    backgroundSize: "cover",
    backgroundPosition: "center",
    paddingLeft: "30px",
    paddingRight: "30px",
    maxWidth: "100%",
    display: "flex",
    flexDirection: "column",
    justifyContent: "flex-end",
    "&::before": {
      content: '""',
      position: "absolute",
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      backgroundColor: color.shadow,
      zIndex: 1,
    },
    "& > *": {
      position: "relative",
      zIndex: 2,
    },
  }));

  if (!article) {
    return (
      <BoxTheme>
        <Box sx={{ color: color.white, padding: "20px" }}>
          <Heading
            variant={TextVariant.H6}
            sx={{
              paddingLeft: "20px",
              color: color.white,
              fontSize: sm ? "24px" : "32px",
            }}
          >
            {t("startArticles.noArticle.label")}
          </Heading>
        </Box>
      </BoxTheme>
    );
  }

  return (
    <NavLink to={RoutePath.ARTICLE.replaceAll(":id", article.id)}>
      <BoxTheme>
        <Box sx={{ color: color.white, padding: "20px", paddingTop: "250px" }}>
          {tags.length > 2
            ? `${tags
                .slice(0, 2)
                .map((tag) =>
                  i18n.language === Language.PL ? tag.plName : tag.engName,
                )
                .join(", ")} ...`
            : tags
                .map((tag) =>
                  i18n.language === Language.PL ? tag.plName : tag.engName,
                )
                .join(", ")}
        </Box>
        <Box sx={{ paddingLeft: "20px" }}>
          <Heading
            variant={TextVariant.H6}
            sx={{
              paddingBottom: "10px",
              color: color.white,
              fontSize: sm ? "24px" : "32px",
              overflow: "hidden",
              textOverflow: "ellipsis",
            }}
          >
            {article.title || ""}
          </Heading>
          <ArticleInfo
            author={article.userAccountUsername || ""}
            date={article.createdAt || new Date()}
          />
        </Box>

        <TextPreview text={article.content || ""} />
      </BoxTheme>
    </NavLink>
  );
};
