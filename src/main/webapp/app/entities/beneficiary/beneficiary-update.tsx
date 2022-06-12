import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHappInitEvnt } from 'app/shared/model/happ-init-evnt.model';
import { getEntities as getHappInitEvnts } from 'app/entities/happ-init-evnt/happ-init-evnt.reducer';
import { IBeneficiary } from 'app/shared/model/beneficiary.model';
import { getEntity, updateEntity, createEntity, reset } from './beneficiary.reducer';

export const BeneficiaryUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const happInitEvnts = useAppSelector(state => state.happInitEvnt.entities);
  const beneficiaryEntity = useAppSelector(state => state.beneficiary.entity);
  const loading = useAppSelector(state => state.beneficiary.loading);
  const updating = useAppSelector(state => state.beneficiary.updating);
  const updateSuccess = useAppSelector(state => state.beneficiary.updateSuccess);
  const handleClose = () => {
    props.history.push('/beneficiary' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getHappInitEvnts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...beneficiaryEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...beneficiaryEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="happinessInitiativesApp.beneficiary.home.createOrEditLabel" data-cy="BeneficiaryCreateUpdateHeading">
            <Translate contentKey="happinessInitiativesApp.beneficiary.home.createOrEditLabel">Create or edit a Beneficiary</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="beneficiary-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('happinessInitiativesApp.beneficiary.eid')}
                id="beneficiary-eid"
                name="eid"
                data-cy="eid"
                type="text"
              />
              <ValidatedField
                label={translate('happinessInitiativesApp.beneficiary.uid')}
                id="beneficiary-uid"
                name="uid"
                data-cy="uid"
                type="text"
              />
              <ValidatedField
                label={translate('happinessInitiativesApp.beneficiary.fullName')}
                id="beneficiary-fullName"
                name="fullName"
                data-cy="fullName"
                type="text"
              />
              <ValidatedField
                label={translate('happinessInitiativesApp.beneficiary.fullNameEn')}
                id="beneficiary-fullNameEn"
                name="fullNameEn"
                data-cy="fullNameEn"
                type="text"
              />
              <ValidatedField
                label={translate('happinessInitiativesApp.beneficiary.grpNumber')}
                id="beneficiary-grpNumber"
                name="grpNumber"
                data-cy="grpNumber"
                type="text"
              />
              <ValidatedField
                label={translate('happinessInitiativesApp.beneficiary.nationality')}
                id="beneficiary-nationality"
                name="nationality"
                data-cy="nationality"
                type="text"
              />
              <ValidatedField
                label={translate('happinessInitiativesApp.beneficiary.genderId')}
                id="beneficiary-genderId"
                name="genderId"
                data-cy="genderId"
                type="text"
              />
              <ValidatedField
                label={translate('happinessInitiativesApp.beneficiary.birthDate')}
                id="beneficiary-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/beneficiary" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BeneficiaryUpdate;
