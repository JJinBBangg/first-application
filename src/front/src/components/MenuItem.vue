<template>
    <button
            class="menu-item"
            :class="{ 'is-active': isActive ? isActive(): null }"
            @click="action"
            :title="title"
    >
        <svg class="remix">
            <use :xlink:href="`${remixIconUrl}#ri-${icon}`"/>
        </svg>

    </button>
</template>

<script setup>
import remixIconUrl from 'remixicon/fonts/remixicon.symbol.svg'
import axios from "axios";
import {ref} from "vue";

const props = defineProps({
    icon: {
        type: String,
        required: true,
    },
    title: {
        type: String,
        required: true,
    },
    action: {
        type: Function,
        required: true,
    },
    isActive: {
        type: Function,
        default: null,
    },
})

</script>

<style lang="scss">
.menu-item {
  background: transparent;
  border: none;
  border-radius: 0.4rem;
  color: #fff;
  cursor: pointer;
  height: 1.75rem;
  padding: 0.25rem;
  margin-right: 0.25rem;
  width: 1.75rem;

  svg {
    fill: currentColor;
    height: 100%;
    width: 100%;
  }

  &.is-active,
  &:hover {
    background-color: #70B184;
  }
}
</style>