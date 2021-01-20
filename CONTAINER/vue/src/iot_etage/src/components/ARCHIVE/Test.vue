<template>
  <div class="container">
    <div class="row">
      <div class="col">Test</div>
      <div class="col">
        <span class="text-primary">{{ msg }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Test",
  props: {
    msg: String,
  },
};
</script>

<style scoped>
</style>