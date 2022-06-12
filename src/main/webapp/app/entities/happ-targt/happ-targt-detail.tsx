import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './happ-targt.reducer';

export const HappTargtDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const happTargtEntity = useAppSelector(state => state.happTargt.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="happTargtDetailsHeading">
          <Translate contentKey="happinessInitiativesApp.happTargt.detail.title">HappTargt</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{happTargtEntity.id}</dd>
          <dt>
            <span id="targetName">
              <Translate contentKey="happinessInitiativesApp.happTargt.targetName">Target Name</Translate>
            </span>
          </dt>
          <dd>{happTargtEntity.targetName}</dd>
        </dl>
        <Button tag={Link} to="/happ-targt" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/happ-targt/${happTargtEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HappTargtDetail;
