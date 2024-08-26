export enum RoutePath {
  START = "/",
  ARTICLE = "/article/:id",
  ALL_ARTICLES = "/articles",
  ALL_TAGS_ARTICLES = "/articles/:category",
  ALL_TAGS = "/tags",
  SIGN_IN = "/sign-in",
  SIGN_UP = "/sign-up",
  ADD_ARTICLE = "/add-article",
  EDIT_TAGS = "/edit-tags",
  EDIT_ARTICLES_STATUS = "/edit-articles-status",
  PAGE_NOT_FOUND = "*",
}
