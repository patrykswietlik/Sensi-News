// eslint-disable-next-line
export const FIRST_SENTENCE_PATTERN = /[^\.!\?]+[\.!\?]+/;
export const DATE_HOUR_PATTERN = "yyyy-MM-dd HH:mm";
export const RATING_CREATED = "RATING_CREATED";
export const RE_CAPTCHA_HEADER = "g-recaptcha-response";

export enum TextVariant {
  H1 = "h1",
  H2 = "h2",
  H3 = "h3",
  H4 = "h4",
  H5 = "h5",
  H6 = "h6",
  SUBTITLE1 = "subtitle1",
  SUBTITLE2 = "subtitle2",
}

export enum LinkDirection {
  ROW = "row",
  COLUMN = "column",
}

export enum Language {
  PL = "pl",
  EN = "en",
}

export enum MainTagsId {
  SOFTWARE = "1bcdb13f-ddab-4a65-86c8-b7aa6ee42274",
  APPS = "95d661eb-40d4-4f9d-9f69-9f58cbd7f108",
  GADGET = "794fc6b8-2987-4527-8cd4-3f39ee5b93ca",
  TECHNOLOGY = "b21c9052-1a3a-4c5e-8c30-5aeca22fb268",
}

export enum ErrorType {
  USER_ACCOUNT_ALREADY_EXISTS = "USER_ACCOUNT_ALREADY_EXISTS",
  USER_NOT_FOUND = "USER_NOT_FOUND",
  BAD_CREDENTIALS_EXCEPTIION = "BAD_CREDENTIALS_EXCEPTIION",
  JWT_INVALID = "JWT_INVALID",
  RE_CAPTCHA_TOKEN_INVALID = "RE_CAPTCHA_TOKEN_INVALID",
  JWT_EXPIRED = "JWT_EXPIRED",
  TAG_CANNOT_BE_DELETED = "TAG_CANNOT_BE_DELETED",
  TAG_NOT_FOUND = "TAG_NOT_FOUND",
  ARTICLE_NOT_FOUND = "ARTICLE_NOT_FOUND",
}

export enum UserRoles {
  ADMIN = "ADMIN",
  USER = "USER",
}
