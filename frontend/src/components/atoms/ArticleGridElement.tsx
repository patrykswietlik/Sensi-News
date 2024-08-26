import { Box, Skeleton, Typography } from "@mui/material";
import { Article } from "../../requests/articleList";
import React from "react";
import AccessTimeIcon from "@mui/icons-material/AccessTime";

type GridElementProps = {
  widthImg?: string;
};

type GridArticleProps = Article & GridElementProps;

export const GridElement = ({
  title,
  image,
  userAccountUsername,
  content,
  widthImg = "270px",
}: GridArticleProps) => {
  const heightImg = (parseInt(widthImg.replace("px", "")) * 9) / 16;

  const contentLength = content.split(" ").length;
  const estimatedReadingTime = Math.ceil(contentLength / 200);

  return (
    <Box
      sx={{
        position: "relative",
        width: widthImg,
        paddingY: "25px",
        overflow: "hidden",
      }}
    >
      {image ? (
        <img
          style={{
            width: widthImg,
            height: heightImg,
            objectFit: "cover",
          }}
          alt={title}
          src={image}
        />
      ) : (
        <Skeleton variant="rectangular" width={widthImg} />
      )}
      {title ? (
        <Box>
          <Typography
            variant="body1"
            sx={{
              marginTop: "10px",
              fontWeight: "bold",
              wordBreak: "break-word",
            }}
          >
            {title}
          </Typography>
        </Box>
      ) : (
        <Box sx={{ pt: 0.5 }}>
          <Skeleton />
          <Skeleton width="60%" />
        </Box>
      )}
      {userAccountUsername ? (
        <Box sx={{ display: "flex", justifyContent: "space-between" }}>
          <Typography variant="body2" sx={{ color: "text.secondary" }}>
            {userAccountUsername}
          </Typography>
          <Typography
            variant="body2"
            sx={{ color: "text.secondary" }}
            style={{ display: "flex", alignItems: "center" }}
          >
            <AccessTimeIcon sx={{ paddingRight: "5px" }} />
            {estimatedReadingTime} min
          </Typography>
        </Box>
      ) : (
        <Box sx={{ pt: 0.5 }}>
          <Skeleton width="40%" />
        </Box>
      )}
    </Box>
  );
};
