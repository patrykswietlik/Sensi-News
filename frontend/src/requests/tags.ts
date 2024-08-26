import { EndpointConst } from "../constants/EndpointConst";
import { apiClient } from "../utils/apiClient";

export interface Tag {
  id: string;
  engName: string;
  plName: string;
  deletedOn: string;
  totalArticles: number;
  removable: boolean;
}

export const getAllTags = async (): Promise<Tag[]> => {
  const response = await apiClient.get(EndpointConst.TAGS);
  return response.data;
};

export const deleteTag = async (tagId: string): Promise<void> => {
  const url = EndpointConst.TAG_ID.replace(":id", tagId);
  await apiClient.delete(url);
};

export const saveTag = async ({ engName, plName }: Tag) => {
  const response = await apiClient.post(EndpointConst.TAGS, {
    engName,
    plName,
  });
  return response.data;
};

export const getTagById = async (tagId: string): Promise<Tag> => {
  const url = EndpointConst.TAG_ID.replace(":id", tagId);
  const response = await apiClient.get(url);
  return response.data;
};

export const getAllTagsByIds = async (ids: string[]): Promise<Tag[]> => {
  const response = await apiClient.post(EndpointConst.TAGS_BY_IDS, {
    ids,
  });
  return response.data;
};
