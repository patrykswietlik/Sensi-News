import Box from "@mui/material/Box";
import { ScaffoldTemplate } from "../components/templates/ScaffoldTemplate";
import { color } from "../styles/colors";
import { NavLink } from "../components/atoms/Link";
import { RoutePath } from "../router/RoutePath ";
import { useTranslation } from "react-i18next";
import { Heading } from "../components/atoms/Heading";
import { Language, TextVariant } from "../constants/Const";
import React, { useEffect, useState } from "react";
import { getAllTags, Tag } from "../requests/tags";

type SortedTagsArray = {
  id: string;
  name: string;
  totalArticles: number;
  letter: string;
};

export const AllTagsPage = () => {
  const { t, i18n } = useTranslation();
  const tagsArray: SortedTagsArray[] = [];
  const [tags, setTags] = useState<Tag[]>([]);

  const fetchTags = async () => {
    const tags = await getAllTags();
    setTags(tags);
  };

  useEffect(() => {
    fetchTags();
  }, []);

  tags.forEach((item) => {
    const name = i18n.language === Language.PL ? item.plName : item.engName;
    tagsArray.push({
      id: item.id,
      name,
      letter: name.charAt(0).toUpperCase(),
      totalArticles: item.totalArticles,
    });
  });

  const sortedTagsArray = tagsArray.sort((a, b) =>
    a.name.localeCompare(b.name),
  );

  const groupedTags: Record<string, SortedTagsArray[]> = {};
  sortedTagsArray.forEach((tag) => {
    if (!groupedTags[tag.letter]) {
      groupedTags[tag.letter] = [];
    }
    groupedTags[tag.letter].push(tag);
  });

  if (tags.length === 0) {
    return (
      <ScaffoldTemplate>
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            backgroundColor: color.gray25,
            padding: "20px",
            minHeight: "80vh",
            height: "auto",
          }}
        >
          <Heading
            variant={TextVariant.H6}
            sx={{
              paddingLeft: "20px",
              color: color.textPrimary,
              fontSize: "28px",
            }}
          >
            {t("startArticles.noArticle.label")}
          </Heading>
        </Box>
      </ScaffoldTemplate>
    );
  }

  return (
    <ScaffoldTemplate>
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "start",
          backgroundColor: color.gray25,
          minHeight: "80vh",
          height: "auto",
        }}
      >
        <Box sx={{ width: { xs: "0%", md: "5%", lg: "10%", xl: "20%" } }} />
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            width: { xs: "100%", md: "90%", lg: "80%", xl: "60%" },
            flexWrap: "wrap",
            overflow: "auto",
            padding: "20px",
            justifyContent: "center",
          }}
        >
          {Object.keys(groupedTags).map((letter) => (
            <Box key={letter} sx={{ padding: { xs: "10px", md: "40px" } }}>
              <Heading
                variant={TextVariant.H6}
                sx={{ color: color.textPrimary, fontSize: "24px" }}
              >
                {letter}
              </Heading>
              {groupedTags[letter].map((tag) => (
                <Box
                  key={tag.id}
                  sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    marginBottom: "10px",
                  }}
                >
                  <NavLink to={RoutePath.ALL_ARTICLES + "/" + tag.id}>
                    {tag.name}
                  </NavLink>
                  <Heading variant={TextVariant.SUBTITLE1}>
                    {tag.totalArticles}
                  </Heading>
                </Box>
              ))}
            </Box>
          ))}
        </Box>
        <Box sx={{ width: { xs: "0%", md: "5%", lg: "10%", xl: "20%" } }} />
      </Box>
    </ScaffoldTemplate>
  );
};
