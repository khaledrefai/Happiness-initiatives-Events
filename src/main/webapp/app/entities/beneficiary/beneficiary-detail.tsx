import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './beneficiary.reducer';

export const BeneficiaryDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const beneficiaryEntity = useAppSelector(state => state.beneficiary.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="beneficiaryDetailsHeading">
          <Translate contentKey="happinessInitiativesApp.beneficiary.detail.title">Beneficiary</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{beneficiaryEntity.id}</dd>
          <dt>
            <span id="eid">
              <Translate contentKey="happinessInitiativesApp.beneficiary.eid">Eid</Translate>
            </span>
          </dt>
          <dd>{beneficiaryEntity.eid}</dd>
          <dt>
            <span id="uid">
              <Translate contentKey="happinessInitiativesApp.beneficiary.uid">Uid</Translate>
            </span>
          </dt>
          <dd>{beneficiaryEntity.uid}</dd>
          <dt>
            <span id="fullName">
              <Translate contentKey="happinessInitiativesApp.beneficiary.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{beneficiaryEntity.fullName}</dd>
          <dt>
            <span id="fullNameEn">
              <Translate contentKey="happinessInitiativesApp.beneficiary.fullNameEn">Full Name En</Translate>
            </span>
          </dt>
          <dd>{beneficiaryEntity.fullNameEn}</dd>
          <dt>
            <span id="grpNumber">
              <Translate contentKey="happinessInitiativesApp.beneficiary.grpNumber">Grp Number</Translate>
            </span>
          </dt>
          <dd>{beneficiaryEntity.grpNumber}</dd>
          <dt>
            <span id="nationality">
              <Translate contentKey="happinessInitiativesApp.beneficiary.nationality">Nationality</Translate>
            </span>
          </dt>
          <dd>{beneficiaryEntity.nationality}</dd>
          <dt>
            <span id="genderId">
              <Translate contentKey="happinessInitiativesApp.beneficiary.genderId">Gender Id</Translate>
            </span>
          </dt>
          <dd>{beneficiaryEntity.genderId}</dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="happinessInitiativesApp.beneficiary.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>
            {beneficiaryEntity.birthDate ? (
              <TextFormat value={beneficiaryEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/beneficiary" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/beneficiary/${beneficiaryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BeneficiaryDetail;
