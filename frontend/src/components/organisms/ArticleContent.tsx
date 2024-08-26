import { Box, IconButton, Typography } from "@mui/material";
import { useNavigate, useParams } from "react-router-dom";
import { RATING_CREATED, TextVariant, UserRoles } from "../../constants/Const";
import { color } from "../../styles/colors";
import { ArticleInfo } from "../atoms/ArticleInfo";
import { Title } from "../atoms/Title";
import { MustRead } from "../molecules/MustRead";
import { useTheme } from "@mui/material/styles";
import useMediaQuery from "@mui/material/useMediaQuery";
import {
  Article,
  getArticleById,
  rateArticle,
} from "../../requests/articleList";
import React, { useContext, useEffect, useState } from "react";
import { Comments } from "../molecules/Comments/Comments";
import { useTranslation } from "react-i18next";
import FavoriteIcon from "@mui/icons-material/Favorite";
import { AuthContext } from "../../providers/AuthProvider";
import { SnackbarContext } from "../../providers/SnackbarProvider";
import AccessTimeIcon from "@mui/icons-material/AccessTime";
import { UpdateArticleModal } from "../molecules/UpdateArticleModal";
import EditIcon from "@mui/icons-material/Edit";
import { getAllTagsByIds, Tag } from "../../requests/tags";
import { RoutePath } from "../../router/RoutePath ";

export const ArticleContent = () => {
  const { id = "" } = useParams();
  const [article, setArticle] = useState<Article>();
  const theme = useTheme();
  const sm = useMediaQuery(theme.breakpoints.down("sm"));
  const { t } = useTranslation();
  const { loggedUser } = useContext(AuthContext);
  const { showSnackbar } = useContext(SnackbarContext);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [tags, setTags] = useState<Tag[]>([]);
  const navigate = useNavigate();

  const fetchArticle = async () => {
    const article = await getArticleById(id);
    if (article) {
      setArticle(article);
    } else {
      navigate(RoutePath.START);
    }
  };

  const fetchTags = async () => {
    if (article?.tagsIds) {
      const tags = await getAllTagsByIds(article.tagsIds);
      setTags(tags);
    }
  };

  const handleRateArticle = async () => {
    const response = await rateArticle(id);
    if (response === RATING_CREATED) {
      showSnackbar(t("articleContent.ratingCreated.label"));
    } else {
      showSnackbar(t("articleContent.ratingDeleted.label"));
    }
    fetchArticle();
  };

  useEffect(() => {
    fetchArticle();
  }, [id]);

  useEffect(() => {
    fetchTags();
  }, [article]);

  const contentLength = article ? article?.content.split(" ").length : 0;
  const estimatedReadingTime = Math.ceil(contentLength / 200);

  return (
    <Box sx={{ width: { xs: "100%", md: "90%", lg: "80%", xl: "60%" } }}>
      <Box
        sx={{
          backgroundColor: color.white,
          marginTop: "30px",
          marginBottom: "30px",
          padding: sm ? "0px" : "80px",
        }}
      >
        <Box
          sx={{
            position: "relative",
            width: "100%",
            paddingBottom: "56.25%",
            marginBottom: "30px",
          }}
        >
          {article?.image ? (
            <img
              src={article.image}
              alt={article.title}
              style={{
                position: "absolute",
                top: 0,
                left: 0,
                width: "100%",
                height: "100%",
                objectFit: "cover",
              }}
            />
          ) : (
            <Box
              sx={{
                display: "flex",
                justifyContent: "center",
                width: "100%",
                height: "200px",
              }}
            >
              {" "}
              {t("articleContent.noImage.label")}{" "}
            </Box>
          )}
        </Box>

        <Box sx={{ display: "flex", justifyContent: "space-between" }}>
          <Box>
            {loggedUser?.role === UserRoles.ADMIN ||
            loggedUser?.role === UserRoles.USER ? (
              <Box sx={{ display: "flex", alignItems: "center" }}>
                <IconButton onClick={handleRateArticle}>
                  <FavoriteIcon />
                </IconButton>
                {article?.likesCount}
              </Box>
            ) : (
              <Box sx={{ display: "flex", alignItems: "center" }}>
                <IconButton>
                  <FavoriteIcon />
                </IconButton>
                {article?.likesCount}
              </Box>
            )}
          </Box>
          <Typography
            variant="body2"
            sx={{ color: "text.secondary" }}
            style={{
              display: "flex",
              alignItems: "center",
              paddingRight: "10px",
            }}
          >
            <AccessTimeIcon sx={{ paddingRight: "5px" }} />
            {estimatedReadingTime} min
          </Typography>
        </Box>

        <Title variant={TextVariant.H4} fontWeight="bold">
          {article?.title || ""}
        </Title>
        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
            textAlign: "center",
          }}
        >
          <ArticleInfo
            author={article?.userAccountUsername || ""}
            date={article?.createdAt || new Date()}
            contentColor={color.textPrimary}
          />
          {loggedUser?.username === article?.userAccountUsername && (
            <IconButton onClick={() => setShowUpdateModal(true)}>
              <EditIcon />
            </IconButton>
          )}
        </Box>

        <Box sx={{ padding: "20px" }}>{article?.content || ""}</Box>
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            color: color.shadow,
            paddingTop: "10px",
            borderTop: "1px solid gray",
          }}
        >
          {t("articleContent.views.label")}: {article?.views}
        </Box>
      </Box>

      <Box
        sx={{
          backgroundColor: color.white,
          marginBottom: "30px",
          padding: "30px",
        }}
      >
        {article && <Comments articleId={id} />}
      </Box>

      <Box
        sx={{
          backgroundColor: color.white,
          marginBottom: "30px",
          paddingX: sm ? "0px" : "80px",
          paddingTop: "30px",
        }}
      >
        <MustRead />
      </Box>

      <UpdateArticleModal
        open={showUpdateModal}
        onClose={() => setShowUpdateModal(false)}
        initialValues={{
          title: article?.title || "",
          content: article?.content || "",
          image: null,
          tags: tags || [],
          articleId: id || "",
        }}
        fetchData={fetchArticle}
      />
    </Box>
  );
};
