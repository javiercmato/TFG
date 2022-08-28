import {FormattedMessage, useIntl} from "react-intl";
import {useAppDispatch, useAppSelector} from "../../store";
import {ChangeEvent, FormEvent, useState} from "react";
import {userRedux} from "../Application";
import {Button, Container, Form, FormControl, FormGroup, Row} from "react-bootstrap";
import {descriptionTextarea} from "../../Recipes/Components/styles/createRecipeForm";
import {CreatePrivateListParamsDTO} from "../Infrastructure";


const CreatePrivateList = () => {
    const intl = useIntl();
    const dispatch = useAppDispatch();
    const [title, setTitle] = useState<string>('');
    const [description, setDescription] = useState<string>('');
    const userID = useAppSelector(userRedux.selectors.selectUserID);
    const privateLists = useAppSelector(userRedux.selectors.selectPrivateLists);

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        let params: CreatePrivateListParamsDTO = {
            title: title,
            description: description,
        };

        let onSuccess: CallbackFunction = () => {}
        let onError: CallbackFunction = () => {}
        dispatch(userRedux.actions.createPrivateListAsyncAction(userID, params, onSuccess, onError));
    }

    return (
        <Container>
            <Form onSubmit={handleSubmit}>
                <Row>
                    <FormGroup>
                        <Form.Label>
                            <b><FormattedMessage id='common.fields.name' /></b>
                        </Form.Label>
                        <FormControl
                            as="input"
                            type="text"
                            value={title}
                            placeholder={intl.formatMessage({id: 'users.components.CreatePrivateList.title.placeholder'})}
                            onChange={(e: ChangeEvent<HTMLInputElement>) => setTitle(e.target.value)}
                        />
                    </FormGroup>
                </Row>

                <Row>
                    <FormGroup>
                        <Form.Label>
                            <b><FormattedMessage id='common.fields.description' /></b>
                        </Form.Label>
                        <Form.Control as="textarea" rows={3} cols={50}
                            value={description}
                            onChange={(e: ChangeEvent<HTMLInputElement>) => setDescription(e.target.value)}
                            placeholder={intl.formatMessage({id: 'users.components.CreatePrivateList.description.placeholder'})}
                            style={descriptionTextarea}
                        />
                    </FormGroup>
                </Row>

                <br/>

                <Row>
                    <Button type="submit">
                        <FormattedMessage id='users.components.CreatePrivateList.createListButton' />
                    </Button>
                </Row>
            </Form>
        </Container>
    )
}


export default CreatePrivateList;
