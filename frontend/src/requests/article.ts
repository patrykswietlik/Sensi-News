import { EndpointConst } from "../constants/EndpointConst";
import { apiClient } from "../utils/apiClient";
import { Tag } from "../models/tag";
import { Article } from "./articleList";

export interface ArticleCreateFormValues {
  title: string;
  content: string;
  image: string | null;
  tags: Tag[];
  articleId: string;
}

export interface ContentGenerationFormValues {
  input: string;
}

export enum ArticleStatusEnum {
  APPROVED = "APPROVED",
  PENDING = "PENDING",
  REJECTED = "REJECTED",
}

export interface ArticleSetStatusForm {
  status: ArticleStatusEnum;
  articleId: string;
}

export const createArticle = async ({
  title,
  content,
  image,
  tags,
}: ArticleCreateFormValues) => {
  const payload = {
    title,
    content,
    image,
    tagsIds: tags.map((tag) => tag.id),
  };

  const response = await apiClient.post(EndpointConst.ARTICLES, payload);

  return response.data;
};

export const generateContent = async ({
  input,
}: ContentGenerationFormValues) => {
  const response = await apiClient.post(EndpointConst.GENERATE, {
    input,
  });

  return response.data;
};

export const setArticleStatus = async ({
  status,
  articleId,
}: ArticleSetStatusForm) => {
  const url = EndpointConst.ARTICLES_STATUS.replace(":id", articleId);
  const response = await apiClient.patch(url, { status });

  return response.data;
};

export const updateArticle = async ({
  title,
  content,
  image,
  tags,
  articleId,
}: ArticleCreateFormValues) => {
  const payload = {
    title,
    content,
    image,
    tagsIds: tags.map((tag) => tag.id),
  };
  const url = EndpointConst.ARTICLE_ID.replace(":id", articleId);
  const response = await apiClient.put(url, payload);

  return response.data;
};

export const deleteArticle = async (articleId: string): Promise<Article> => {
  const url = EndpointConst.ARTICLE_ID.replace(":id", articleId);
  const response = await apiClient.delete(url);
  return response.data;
};
