import { Box } from "@mui/material";
import { TextVariant } from "../../constants/Const";
import { Heading } from "./Heading";
import { NavLink } from "./Link";

type TitleProps = {
  articleId?: number;
  children: string;
  color?: string;
  variant?: TextVariant;
  fontSize?: string;
  fontWeight?: string;
  destination?: string;
};

export const Title = ({
  children,
  color,
  variant = TextVariant.H6,
  fontSize,
  fontWeight,
  destination: to = "",
}: TitleProps) => {
  return (
    <Box>
      <Heading
        variant={variant}
        sx={{
          paddingBottom: "5px",
        }}
      >
        <NavLink
          sx={{
            color: color,
            fontSize: fontSize,
            fontWeight: fontWeight,
            wordBreak: "break-word",
          }}
          to={to}
        >
          {children}
        </NavLink>
      </Heading>
    </Box>
  );
};
