import {container, titleText} from './styles/privateListSummary';
import {Button, Col, Container, Row} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import PrivateListSummaryDTO from "../Infrastructure/PrivateListSummaryDTO";

interface Props {
    privateList: PrivateListSummaryDTO,
    onOpenDetailsClick: (listId: string) => void,
}

const PrivateListSummary = ({privateList, onOpenDetailsClick}: Props) => {

    return (
        <Container style={container}>
            <Row>
                <Col md={8} className={"my-auto"}>
                    <span style={titleText}>
                        {privateList.title}
                    </span>
                </Col>

                <Col>
                    <Button variant="info" onClick={() => onOpenDetailsClick(privateList.id)}>
                        <FormattedMessage id="common.buttons.open" />
                    </Button>
                </Col>
            </Row>
        </Container>
    )
}

export type {Props as PrivateListSummaryProps};
export default PrivateListSummary;
