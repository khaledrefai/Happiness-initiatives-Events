import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './happ-evnt-typ.reducer';

export const HappEvntTypDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const happEvntTypEntity = useAppSelector(state => state.happEvntTyp.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="happEvntTypDetailsHeading">
          <Translate contentKey="happinessInitiativesApp.happEvntTyp.detail.title">HappEvntTyp</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{happEvntTypEntity.id}</dd>
          <dt>
            <span id="eventName">
              <Translate contentKey="happinessInitiativesApp.happEvntTyp.eventName">Event Name</Translate>
            </span>
          </dt>
          <dd>{happEvntTypEntity.eventName}</dd>
        </dl>
        <Button tag={Link} to="/happ-evnt-typ" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/happ-evnt-typ/${happEvntTypEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HappEvntTypDetail;
