interface Props {
    link : string,
    text : string,

}

/** Enlace a páginas externas a la aplicación */
const ExternalLink = ({link, text}: Props) => (
    <a href={link}
       target="_blank"
       rel="noopener noreferrer"
    >
        {text}
    </a>
)


export default ExternalLink;
