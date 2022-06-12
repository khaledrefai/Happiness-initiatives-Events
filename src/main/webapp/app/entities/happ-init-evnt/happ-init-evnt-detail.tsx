import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './happ-init-evnt.reducer';

export const HappInitEvntDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const happInitEvntEntity = useAppSelector(state => state.happInitEvnt.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="happInitEvntDetailsHeading">
          <Translate contentKey="happinessInitiativesApp.happInitEvnt.detail.title">HappInitEvnt</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{happInitEvntEntity.id}</dd>
          <dt>
            <span id="initEventName">
              <Translate contentKey="happinessInitiativesApp.happInitEvnt.initEventName">Init Event Name</Translate>
            </span>
          </dt>
          <dd>{happInitEvntEntity.initEventName}</dd>
          <dt>
            <span id="dateFrom">
              <Translate contentKey="happinessInitiativesApp.happInitEvnt.dateFrom">Date From</Translate>
            </span>
          </dt>
          <dd>
            {happInitEvntEntity.dateFrom ? (
              <TextFormat value={happInitEvntEntity.dateFrom} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dateTo">
              <Translate contentKey="happinessInitiativesApp.happInitEvnt.dateTo">Date To</Translate>
            </span>
          </dt>
          <dd>
            {happInitEvntEntity.dateTo ? <TextFormat value={happInitEvntEntity.dateTo} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="totalTarget">
              <Translate contentKey="happinessInitiativesApp.happInitEvnt.totalTarget">Total Target</Translate>
            </span>
          </dt>
          <dd>{happInitEvntEntity.totalTarget}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="happinessInitiativesApp.happInitEvnt.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{happInitEvntEntity.notes}</dd>
          <dt>
            <span id="initOrEvent">
              <Translate contentKey="happinessInitiativesApp.happInitEvnt.initOrEvent">Init Or Event</Translate>
            </span>
          </dt>
          <dd>{happInitEvntEntity.initOrEvent}</dd>
          <dt>
            <span id="addDate">
              <Translate contentKey="happinessInitiativesApp.happInitEvnt.addDate">Add Date</Translate>
            </span>
          </dt>
          <dd>
            {happInitEvntEntity.addDate ? (
              <TextFormat value={happInitEvntEntity.addDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="addBy">
              <Translate contentKey="happinessInitiativesApp.happInitEvnt.addBy">Add By</Translate>
            </span>
          </dt>
          <dd>{happInitEvntEntity.addBy}</dd>
          <dt>
            <Translate contentKey="happinessInitiativesApp.happInitEvnt.happEvntTyp">Happ Evnt Typ</Translate>
          </dt>
          <dd>{happInitEvntEntity.happEvntTyp ? happInitEvntEntity.happEvntTyp.id : ''}</dd>
          <dt>
            <Translate contentKey="happinessInitiativesApp.happInitEvnt.happInitTyp">Happ Init Typ</Translate>
          </dt>
          <dd>{happInitEvntEntity.happInitTyp ? happInitEvntEntity.happInitTyp.id : ''}</dd>
          <dt>
            <Translate contentKey="happinessInitiativesApp.happInitEvnt.happTargt">Happ Targt</Translate>
          </dt>
          <dd>{happInitEvntEntity.happTargt ? happInitEvntEntity.happTargt.id : ''}</dd>
          <dt>
            <Translate contentKey="happinessInitiativesApp.happInitEvnt.happEvntPlac">Happ Evnt Plac</Translate>
          </dt>
          <dd>{happInitEvntEntity.happEvntPlac ? happInitEvntEntity.happEvntPlac.id : ''}</dd>
          <dt>
            <Translate contentKey="happinessInitiativesApp.happInitEvnt.employee">Employee</Translate>
          </dt>
          <dd>
            {happInitEvntEntity.employees
              ? happInitEvntEntity.employees.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.slmName}</a>
                    {happInitEvntEntity.employees && i === happInitEvntEntity.employees.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="happinessInitiativesApp.happInitEvnt.beneficiary">Beneficiary</Translate>
          </dt>
          <dd>
            {happInitEvntEntity.beneficiaries
              ? happInitEvntEntity.beneficiaries.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.fullName}</a>
                    {happInitEvntEntity.beneficiaries && i === happInitEvntEntity.beneficiaries.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/happ-init-evnt" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/happ-init-evnt/${happInitEvntEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HappInitEvntDetail;
