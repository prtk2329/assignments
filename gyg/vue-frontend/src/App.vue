<script>
import ActivitySearch from './components/ActivitySearch.vue';

export default {
  name: 'App',
  components: {
    ActivitySearch
  },
}
</script>

<template>
  <div id="app">
    <h1>Activity Search</h1>
      <ActivitySearch/>
  </div>
</template>

<style>
#app {
  text-align: center;
  color: #2c3e50;
}
</style>
