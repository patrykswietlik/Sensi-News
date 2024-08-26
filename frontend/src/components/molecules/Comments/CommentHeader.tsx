import { Heading } from "../../atoms/Heading";
import { DATE_HOUR_PATTERN, TextVariant } from "../../../constants/Const";
import { color } from "../../../styles/colors";
import { format } from "date-fns";
import { Box } from "@mui/material";
import React from "react";
import { useTranslation } from "react-i18next";

type CommentsHeaderProps = {
  author: string;
  date: string;
  version: number;
};

export const CommentHeader = ({
  author,
  date,
  version,
}: CommentsHeaderProps) => {
  const { t } = useTranslation();

  return (
    <Box sx={{ display: "flex", alignItems: "center" }}>
      <Heading
        variant={TextVariant.SUBTITLE2}
        sx={{ color: color.shadow, padding: "0px" }}
      >
        {author} {format(new Date(date), DATE_HOUR_PATTERN)}
      </Heading>
      {version !== 0 && (
        <Heading
          variant={TextVariant.SUBTITLE2}
          sx={{
            color: color.shadow50,
            padding: 0,
            paddingLeft: "10px",
            fontStyle: "italic",
          }}
        >
          {t("articleContent.edited.label")}
        </Heading>
      )}
    </Box>
  );
};
