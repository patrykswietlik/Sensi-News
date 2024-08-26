import CheckOutlinedIcon from "@mui/icons-material/CheckOutlined";
import CloseOutlinedIcon from "@mui/icons-material/CloseOutlined";
import VisibilityOutlinedIcon from "@mui/icons-material/VisibilityOutlined";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { ConfirmModal } from "../../components/molecules/ConfirmModal";
import { Article, getAllPendingArticles } from "../../requests/articleList";
import { ArticleStatusEnum, setArticleStatus } from "../../requests/article";
import {
  ArticlePreviewModal,
  ArticlePreviewProps,
} from "../../components/molecules/ArticlePreviewModal";
import { GenericTable } from "../../components/molecules/GenericTable";

export const TableForm = () => {
  const [articles, setArticles] = useState<Article[]>([]);
  const { t } = useTranslation();
  const [openConfirmationModal, setOpenConfirmationModal] = useState(false);
  const [openImageDisplayModal, setOpenImageDisplayModal] = useState(false);
  const [selectedArticle, setSelectedArticle] =
    useState<ArticlePreviewProps | null>(null);
  const [selectedAction, setSelectedAction] =
    useState<ArticleStatusEnum | null>(null);
  const fetchArticles = async () => {
    const articles = await getAllPendingArticles();
    setArticles(articles);
  };

  useEffect(() => {
    fetchArticles();
  }, []);

  const handleImageDisplay = ({
    id,
    author,
    title,
    content,
    image,
    date,
  }: ArticlePreviewProps) => {
    setSelectedArticle({ id, author, title, content, image, date });
    setOpenImageDisplayModal(true);
  };

  const handleModalClose = () => {
    setSelectedAction(null);
    setOpenConfirmationModal(false);
    setOpenImageDisplayModal(false);
  };

  const handleModalConfirm = () => {
    if (selectedAction && selectedArticle && selectedArticle.id) {
      handleSetArticleStatus(selectedAction, { id: selectedArticle.id });
    }

    handleModalClose();
  };

  const handleFormatDate = (date: Date) => {
    const formattedDate = new Date(date);
    return (
      <span>
        {formattedDate.toLocaleDateString()}
        <br />
        {formattedDate.toLocaleTimeString()}
      </span>
    );
  };

  const handleSetArticleStatus = async (
    status: ArticleStatusEnum,
    { id }: ArticlePreviewProps,
  ) => {
    await setArticleStatus({ status, articleId: id! });
    fetchArticles();
  };

  const transformedArticles = articles.map((article) => {
    return {
      id: article.id,
      author: article.userAccountUsername,
      title: article.title,
      content: article.content,
      image: article.image,
      createdAt: handleFormatDate(article.createdAt),
    };
  });

  return (
    <GenericTable
      labels={[
        {
          label: t("editArticlesStatus.id.label"),
          show: false,
        },
        {
          label: t("editArticlesStatus.userAccountUsername.label"),
          show: true,
        },
        {
          label: t("editArticlesStatus.title.label"),
          show: true,
        },
        {
          label: t("editArticlesStatus.content.label"),
          show: true,
        },
        {
          label: t("editArticlesStatus.image.label"),
          show: false,
        },
        {
          label: t("editArticlesStatus.createdAt.label"),
          show: true,
        },
      ]}
      data={transformedArticles}
      actions={[
        {
          icon: <VisibilityOutlinedIcon />,
          onClick: (row) =>
            handleImageDisplay({
              ...row,
            }),
        },
        {
          icon: <CheckOutlinedIcon />,
          onClick: (row) => {
            setSelectedAction(ArticleStatusEnum.APPROVED);
            setSelectedArticle({ ...row });
            setOpenConfirmationModal(true);
          },
        },
        {
          icon: <CloseOutlinedIcon />,
          onClick: (row) => {
            setSelectedAction(ArticleStatusEnum.REJECTED);
            setSelectedArticle({ ...row });
            setOpenConfirmationModal(true);
          },
        },
      ]}
    >
      <ArticlePreviewModal
        open={openImageDisplayModal}
        onClose={handleModalClose}
        {...selectedArticle}
      />
      <ConfirmModal
        text={
          selectedAction === ArticleStatusEnum.APPROVED
            ? t("editArticlesStatus.approveConfirmation.label")
            : t("editArticlesStatus.rejectConfirmation.label")
        }
        open={openConfirmationModal}
        onClose={handleModalClose}
        onConfirm={handleModalConfirm}
      />
    </GenericTable>
  );
};
