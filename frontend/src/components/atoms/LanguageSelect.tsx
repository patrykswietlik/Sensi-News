import { Box, MenuItem, Select, SelectChangeEvent } from "@mui/material";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import FormControl from "@mui/material/FormControl";

type LanguageSelectProps = {
  displayXs: string;
  displayMd: string;
};

export const LanguageSelect = ({
  displayXs,
  displayMd,
}: LanguageSelectProps) => {
  const { t, i18n } = useTranslation();
  const [currentLang, setCurrentLang] = useState<string>(
    i18n.resolvedLanguage ?? "",
  );

  const handleChange = (event: SelectChangeEvent<string>) => {
    const newLang = event.target.value;
    i18n.changeLanguage(newLang);
    setCurrentLang(newLang);
  };

  const lngs: { [key: string]: { nativeName: string } } = {
    en: { nativeName: t("navbar.languageENG.label") },
    pl: { nativeName: t("navbar.languagePL.label") },
  };

  return (
    <Box
      sx={{
        display: { xs: displayXs, md: displayMd },
        justifyContent: "flex-end",
        alignItems: "center",
      }}
    >
      <FormControl sx={{ m: 0, minWidth: 100 }} size="small">
        <Select
          labelId="demo-select-small-label"
          id="demo-select-small"
          value={currentLang}
          onChange={handleChange}
          variant="outlined"
          sx={{ marginRight: "20px", fontSize: "14px", height: "auto" }}
        >
          {Object.keys(lngs).map((lng) => (
            <MenuItem key={lng} value={lng} sx={{ fontSize: "14px" }}>
              {lngs[lng].nativeName}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    </Box>
  );
};
