<template>
  <div
    :class="[
      'el-color-picker',
      colorDisabled ? 'is-disabled' : '',
      colorSize ? `el-color-picker--${ colorSize }` : ''
    ]"
    v-clickoutside="hide">
    <div class="el-color-picker__mask" v-if="colorDisabled"></div>
    <div class="el-color-picker__trigger" @click="handleTrigger">
      <span class="el-color-picker__color" :class="{ 'is-alpha': showAlpha }">
        <span class="el-color-picker__color-inner"
          :style="{
            backgroundColor: displayedColor
          }"></span>
        <span class="el-color-picker__empty el-icon-close" v-if="!value && !showPanelColor"></span>
      </span>
      <span class="el-color-picker__icon el-icon-arrow-down" v-show="value || showPanelColor"></span>
    </div>
    <picker-dropdown
       ref="dropdown"
       :class="['el-color-picker__panel', popperClass || '']"
       v-model="showPicker"
       @pick="confirmValue"
       @clear="clearValue"
       :color="color"
       :show-alpha="showAlpha"
       :predefine="predefine">
    </picker-dropdown>
  </div>
</template>

<script>
  import Color from './color';
  import PickerDropdown from './components/picker-dropdown.vue';
  import Clickoutside from 'static/element-ui/src/utils/clickoutside';
  import Emitter from 'static/element-ui/src/mixins/emitter';

  export default {
    name: 'ElColorPicker',

    mixins: [Emitter],

    props: {
      value: String,
      showAlpha: Boolean,
      colorFormat: String,
      disabled: Boolean,
      size: String,
      popperClass: String,
      predefine: Array
    },

    inject: {
      elForm: {
        default: ''
      },
      elFormItem: {
        default: ''
      }
    },

    directives: { Clickoutside },

    computed: {
      displayedColor() {
        if (!this.value && !this.showPanelColor) {
          return 'transparent';
        }

        return this.displayedRgb(this.color, this.showAlpha);
      },

      _elFormItemSize() {
        return (this.elFormItem || {}).elFormItemSize;
      },

      colorSize() {
        return this.size || this._elFormItemSize || (this.$ELEMENT || {}).size;
      },

      colorDisabled() {
        return this.disabled || (this.elForm || {}).disabled;
      }
    },

    watch: {
      value(val) {
        if (!val) {
          this.showPanelColor = false;
        } else if (val && val !== this.color.value) {
          this.color.fromString(val);
        }
      },
      color: {
        deep: true,
        handler() {
          this.showPanelColor = true;
        }
      },
      displayedColor(val) {
        if (!this.showPicker) return;
        const currentValueColor = new Color({
          enableAlpha: this.showAlpha,
          format: this.colorFormat
        });
        currentValueColor.fromString(this.value);

        const currentValueColorRgb = this.displayedRgb(currentValueColor, this.showAlpha);
        if (val !== currentValueColorRgb) {
          this.$emit('active-change', val);
        }
      }
    },

    methods: {
      handleTrigger() {
        if (this.colorDisabled) return;
        this.showPicker = !this.showPicker;
      },
      confirmValue() {
        const value = this.color.value;
        this.$emit('input', value);
        this.$emit('change', value);
        this.dispatch('ElFormItem', 'el.form.change', value);
        this.showPicker = false;
      },
      clearValue() {
        this.$emit('input', null);
        this.$emit('change', null);
        if (this.value !== null) {
          this.dispatch('ElFormItem', 'el.form.change', null);
        }
        this.showPanelColor = false;
        this.showPicker = false;
        this.resetColor();
      },
      hide() {
        this.showPicker = false;
        this.resetColor();
      },
      resetColor() {
        this.$nextTick(_ => {
          if (this.value) {
            this.color.fromString(this.value);
          } else {
            this.showPanelColor = false;
          }
        });
      },
      displayedRgb(color, showAlpha) {
        if (!(color instanceof Color)) {
          throw Error('color should be instance of Color Classes');
        }

        const { r, g, b } = color.toRgb();
        return showAlpha
          ? `rgba(${ r }, ${ g }, ${ b }, ${ color.get('alpha') / 100 })`
          : `rgb(${ r }, ${ g }, ${ b })`;
      }
    },

    mounted() {
      const value = this.value;
      if (value) {
        this.color.fromString(value);
      }
      this.popperElm = this.$refs.dropdown.$el;
    },

    data() {
      const color = new Color({
        enableAlpha: this.showAlpha,
        format: this.colorFormat
      });

      return {
        color,
        showPicker: false,
        showPanelColor: false
      };
    },

    components: {
      PickerDropdown
    }
  };
</script>
