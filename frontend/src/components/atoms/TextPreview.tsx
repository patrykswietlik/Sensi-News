import { FIRST_SENTENCE_PATTERN } from "../../constants/Const";
import { color } from "../../styles/colors";

interface TextPreviewProps {
  text: string;
}

export const TextPreview = (props: TextPreviewProps) => {
  const getPreviewText = (text: string) => {
    const firstSentence = text.match(FIRST_SENTENCE_PATTERN) || "";
    return firstSentence[0] || "";
  };

  return (
    <p
      style={{
        paddingLeft: "20px",
        paddingBottom: "30px",
        color: color.white,
        fontWeight: "normal",
      }}
    >
      {getPreviewText(props.text)}
    </p>
  );
};
