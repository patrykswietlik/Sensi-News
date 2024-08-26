import React from "react";
import Typography from "@mui/material/Typography";
import { TextVariant } from "../../constants/Const";
import { color } from "../../styles/colors";

type HeadingProps = {
  variant: TextVariant;
  children: React.ReactNode;
  sx?: object;
};

export const Heading = ({ variant, children, sx }: HeadingProps) => {
  return (
    <Typography
      variant={variant || TextVariant.H6}
      component="div"
      sx={{
        fontFamily: "Inter, sans-serif",
        paddingTop: "5px",
        color: color.textPrimary,
        ...sx,
      }}
    >
      {children}
    </Typography>
  );
};
