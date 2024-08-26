import { EndpointConst } from "../constants/EndpointConst";
import { apiClient } from "../utils/apiClient";

export interface Article {
  id: string;
  title: string;
  userAccountId: string;
  userAccountUsername: string;
  content: string;
  image: string;
  createdAt: Date;
  updatedAt: Date;
  tagsIds: string[];
  views: number;
  likesCount: number;
}

export interface Comment {
  id: string;
  content: string;
  articleId: string;
  userAccountId: string;
  userAccountUsername: string;
  replyToId: string;
  replies: Comment[];
  createdAt: string;
  updatedAt: string;
  version: number;
}

export const getAllArticles = async (): Promise<Article[]> => {
  const response = await apiClient.get(EndpointConst.ARTICLES_SEARCH);
  return response.data;
};

export const getAllPendingArticles = async (): Promise<Article[]> => {
  const response = await apiClient.get(EndpointConst.ARTICLES_PENDING);
  return response.data;
};

export const getArticlesByTag = async (tagId: string): Promise<Article[]> => {
  const response = await apiClient.get(EndpointConst.ARTICLES_BY_TAGS, {
    params: {
      tags: tagId,
    },
  });
  return response.data;
};

export const getArticleById = async (
  articleId: string,
): Promise<Article | null> => {
  const url = EndpointConst.ARTICLE_ID.replace(":id", articleId);
  const response = await apiClient.get(url);
  return response?.data;
};

export const getArticleComments = async (id: string): Promise<Comment[]> => {
  const url = EndpointConst.ARTICLE_COMMENTS.replace(":id", id);
  const response = await apiClient.get(url);
  return response.data;
};

export const createComment = async (
  articleId: string,
  content: string,
): Promise<Comment> => {
  const response = await apiClient.post(EndpointConst.COMMENTS, {
    content,
    articleId,
  });
  return response.data;
};

export const deleteComment = async (id: string): Promise<void> => {
  const url = EndpointConst.COMMENTS_ID.replace(":id", id);
  const response = await apiClient.delete(url);
  return response.data;
};

export const updateComment = async (
  articleId: string,
  commentId: string,
  content: string,
): Promise<void> => {
  const url = EndpointConst.COMMENTS_ID.replace(":id", commentId);
  const response = await apiClient.put(url, {
    content,
    articleId,
  });
  return response.data;
};

export const commentReply = async (
  content: string,
  articleId: string,
  replyToId: string,
): Promise<void> => {
  const response = await apiClient.post(EndpointConst.COMMENTS_REPLY, {
    content,
    articleId,
    replyToId,
  });
  return response.data;
};

export const rateArticle = async (articleId: string): Promise<string> => {
  const url = EndpointConst.ARTICLE_LIKE.replace(":id", articleId);
  const response = await apiClient.post(url);
  return response.data.successType;
};
