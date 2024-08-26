import { Tag } from "./tag";

export interface Article {
  id: string;
  title: string;
  author: string;
  content: string;
  date: Date;
  tags: Tag[];
  img: string;
}
