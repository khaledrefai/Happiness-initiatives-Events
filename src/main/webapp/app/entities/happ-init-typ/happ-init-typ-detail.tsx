import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './happ-init-typ.reducer';

export const HappInitTypDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const happInitTypEntity = useAppSelector(state => state.happInitTyp.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="happInitTypDetailsHeading">
          <Translate contentKey="happinessInitiativesApp.happInitTyp.detail.title">HappInitTyp</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{happInitTypEntity.id}</dd>
          <dt>
            <span id="initName">
              <Translate contentKey="happinessInitiativesApp.happInitTyp.initName">Init Name</Translate>
            </span>
          </dt>
          <dd>{happInitTypEntity.initName}</dd>
        </dl>
        <Button tag={Link} to="/happ-init-typ" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/happ-init-typ/${happInitTypEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HappInitTypDetail;
