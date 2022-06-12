import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/happ-evnt-typ">
        <Translate contentKey="global.menu.entities.happEvntTyp" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/happ-init-typ">
        <Translate contentKey="global.menu.entities.happInitTyp" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/happ-targt">
        <Translate contentKey="global.menu.entities.happTargt" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/department">
        <Translate contentKey="global.menu.entities.department" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/employee">
        <Translate contentKey="global.menu.entities.employee" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/happ-evnt-plac">
        <Translate contentKey="global.menu.entities.happEvntPlac" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/beneficiary">
        <Translate contentKey="global.menu.entities.beneficiary" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/happ-init-evnt">
        <Translate contentKey="global.menu.entities.happInitEvnt" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/attachment">
        <Translate contentKey="global.menu.entities.attachment" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
