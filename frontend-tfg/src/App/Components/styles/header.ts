import {CSSProperties} from "react";

export const navbar: CSSProperties = {
    alignItems: "center",
}

export const headerLink: CSSProperties = {
    textDecoration: 'none',
}

export const headerUserAccessLinks: CSSProperties = {
    display: 'block',
    flexDirection: 'row',
}

export const navItem: CSSProperties = {
    marginLeft: "1em",
    marginRight: "1em"
}

export const navGlobalItem: CSSProperties = {
    ...navItem,
    justifyContent: "space-between"
}
