<template>
  <li
    class="el-dropdown-menu__item"
    :class="{
      'is-disabled': disabled,
      'el-dropdown-menu__item--divided': divided
    }"
    @click="handleClick"
    :aria-disabled="disabled"
    :tabindex="disabled ? null : -1"
  >
    <i :class="icon" v-if="icon"></i>
    <slot></slot>
  </li>
</template>
<script>
  import Emitter from 'static/element-ui/src/mixins/emitter';

  export default {
    name: 'ElDropdownItem',

    mixins: [Emitter],

    props: {
      command: {},
      disabled: Boolean,
      divided: Boolean,
      icon: String
    },

    methods: {
      handleClick(e) {
        this.dispatch('ElDropdown', 'menu-item-click', [this.command, this]);
      }
    }
  };
</script>
