import Link from "@mui/material/Link";
import React from "react";
import {
  Link as RouterLink,
  LinkProps as RouterLinkProps,
} from "react-router-dom";
import { color } from "../../styles/colors";

type NavLinkProps = RouterLinkProps & {
  color?: string;
  sx?: object;
  component?: React.ForwardRefExoticComponent<
    RouterLinkProps & React.RefAttributes<HTMLAnchorElement>
  >;
  hoverColor?: string;
};

export const NavLink = (props: NavLinkProps) => {
  return (
    <Link
      component={RouterLink || props.component}
      to={props.to}
      color={props.color || color.textPrimary}
      sx={{
        textDecoration: "none",
        marginRight: "32px",
        fontWeight: "medium",
        "&:hover": {
          color: color.textSecondary,
        },
        ...props.sx,
      }}
    >
      {props.children}
    </Link>
  );
};
