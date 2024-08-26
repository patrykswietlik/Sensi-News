import { Box } from "@mui/material";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Language, TextVariant } from "../../constants/Const";
import { Article } from "../../requests/articleList";
import { getAllTagsByIds } from "../../requests/tags";
import { RoutePath } from "../../router/RoutePath ";
import { color } from "../../styles/colors";
import { Heading } from "../atoms/Heading";
import { TagHeading } from "../atoms/TagHeading";
import { Title } from "../atoms/Title";

interface SideArticlesProps {
  articles: Article[];
}

export const SideArticles = ({ articles }: SideArticlesProps) => {
  const { t, i18n } = useTranslation();
  const [tags, setTags] = useState<{
    [key: string]: { plName: string; engName: string }[];
  }>({});

  useEffect(() => {
    const fetchTags = async () => {
      const tagsMap: { [key: string]: { plName: string; engName: string }[] } =
        {};
      for (const article of articles) {
        if (article?.tagsIds) {
          const fetchedTags = await getAllTagsByIds(article.tagsIds);
          tagsMap[article.id] = fetchedTags;
        }
      }
      setTags(tagsMap);
    };

    fetchTags();
  }, [articles]);

  if (articles.length === 0) {
    return (
      <Box sx={{ color: color.white, padding: "20px" }}>
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
    );
  }

  return (
    <>
      {articles.map((item, index) => (
        <Box
          key={index}
          sx={{
            paddingBottom: "20px",
            paddingLeft: "40px",
          }}
        >
          <TagHeading
            color={color.textSecondary}
            tagName={
              tags[item.id]?.length > 2
                ? `${tags[item.id]
                    .slice(0, 2)
                    .map((tag) =>
                      i18n.language === Language.PL ? tag.plName : tag.engName,
                    )
                    .join(", ")} ...`
                : tags[item.id]
                    ?.map((tag) =>
                      i18n.language === Language.PL ? tag.plName : tag.engName,
                    )
                    .join(", ")
            }
          />

          <Title
            variant={TextVariant.SUBTITLE1}
            fontWeight="bold"
            destination={RoutePath.ARTICLE.replaceAll(":id", item.id)}
          >
            {item.title || ""}
          </Title>
        </Box>
      ))}
    </>
  );
};
