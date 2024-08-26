import { Box, useMediaQuery, useTheme } from "@mui/material";
import { useEffect, useState } from "react";
import { MainArticle } from "../../components/molecules/MainArticle";
import { SideArticles } from "../../components/molecules/SideArticles";
import { TrilerOfArticles } from "../../components/molecules/TrilerOfArticles";
import { Article, getAllArticles } from "../../requests/articleList";
import { Heading } from "../atoms/Heading";
import { color } from "../../styles/colors";
import { TextVariant } from "../../constants/Const";
import { useTranslation } from "react-i18next";
import { LoadingSpinner } from "../atoms/LoadingSpinner";

export const StartArticles = () => {
  const [content, setContent] = useState<Article[]>([]);
  const { t } = useTranslation();
  const theme = useTheme();
  const sm = useMediaQuery(theme.breakpoints.down("sm"));
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

  if (isLoading) {
    return <LoadingSpinner />;
  }

  if (content.length === 0) {
    return (
      <Box
        sx={{
          width: "100%",
          color: color.white,
          padding: "20px",
        }}
      >
        <Heading
          variant={TextVariant.H6}
          sx={{
            textAlign: "center",
            color: color.textPrimary,
            fontSize: sm ? "24px" : "32px",
          }}
        >
          {t("startArticles.noArticle.label")}
        </Heading>
      </Box>
    );
  }

  return (
    <Box sx={{ width: { xs: "100%", md: "90%", lg: "80%", xl: "70%" } }}>
      <Box
        sx={{
          display: "flex",
          flexDirection: { xs: "column", md: "column", lg: "row", xl: "row" },
          height: "auto",
        }}
      >
        <Box sx={{ width: { xs: "100%", lg: "70%" } }}>
          <MainArticle article={content[0]} />
        </Box>

        <Box
          sx={{
            width: { xs: "100%", lg: "30%" },
            marginTop: "20px",
            marginBottom: "20px",
          }}
        >
          <SideArticles articles={content.slice(1, 4)} />
        </Box>
      </Box>

      <Box>
        <TrilerOfArticles articles={content.slice(1, 4)} />
      </Box>
    </Box>
  );
};
