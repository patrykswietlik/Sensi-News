import WarningAmberIcon from "@mui/icons-material/WarningAmber";
import { Box } from "@mui/material";
import { Heading } from "../components/atoms/Heading";
import { TextVariant } from "../constants/Const";
import { useTranslation } from "react-i18next";
import { NavLink } from "../components/atoms/Link";
import { RoutePath } from "../router/RoutePath ";
import { color } from "../styles/colors";

export const PageNotFound = () => {
  const { t } = useTranslation();
  return (
    <Box sx={{ textAlign: "center", paddingTop: "80px" }}>
      <Box sx={{ display: "flex", justifyContent: "center" }}>
        <WarningAmberIcon sx={{ fontSize: 70 }} />
      </Box>
      <Box sx={{ display: "flex", justifyContent: "center" }}>
        <Heading variant={TextVariant.H3}>
          {t("errors.pageNotFound.label")}
        </Heading>
      </Box>
      <Box
        sx={{ display: "flex", justifyContent: "center", paddingTop: "20px" }}
      >
        <NavLink color={color.textSecondary} to={RoutePath.START}>
          {t("errors.backToHome.label")}
        </NavLink>
      </Box>
    </Box>
  );
};
