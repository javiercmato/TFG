import {Button, Pagination} from "react-bootstrap";
import {FormattedMessage} from "react-intl";

interface Props {
    currentPage: number,
    previous: {
        enabled: boolean,
        onClickCallback: any,
    },
    next: {
        enabled: boolean,
        onClickCallback: any
    }
}

const Pager = ({currentPage, previous, next}: Props) => {

    return (
        <Pagination>
            <Button
                disabled={!previous.enabled}
                onClick={previous.onClickCallback}
            >
                <FormattedMessage id='common.buttons.previous' />
            </Button>

            <Pagination.Item className={"disabled"}>
                {currentPage}
            </Pagination.Item>

            <Button
                disabled={!next.enabled}
                onClick={next.onClickCallback}
            >
                <FormattedMessage id='common.buttons.next' />
            </Button>
        </Pagination>
    )
}

export type {Props as PagerProps};
export default Pager;
