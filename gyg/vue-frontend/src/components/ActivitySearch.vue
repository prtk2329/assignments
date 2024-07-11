<script>

export default {
  name: "activity-search-component",
  data() {
      return {
      activities: [],
      searchCriteria: {
        query: '',
        page: 1,
        size: 10, // Default page size
      },
      pagination: {
        totalHits: 0,
        totalPages: 0,
        currentPage: 0,
        pageSize: 0,
        hasNext: false,
        hasPrevious: false,
      },
    };
  },
  async mounted() {
    this.submitSearch()
  },
  methods: {
    submitSearch() {
      this.fetchActivities();
    },
    async fetchActivities() {
      const params = new URLSearchParams(
          Object.entries(this.searchCriteria).reduce((acc, [key, value]) => {
            if (value !== null && value !== undefined) {
              acc[key] = value.toString();
            }
            return acc;
          }, {})
      );

      const response = await fetch(`http://localhost:8080/activity/search?${params}`, {
        method: 'GET',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        const msg = `Error fetching activities: ${response.status} - ${response.statusText}`;
        throw new Error(msg);
      }

      const data = await response.json();
      this.activities = data.content;
      this.pagination.totalHits = data.totalHits;
      this.pagination.totalPages = data.totalPages;
      this.pagination.currentPage = data.currentPage;
      this.pagination.pageSize = data.pageSize;
      this.pagination.hasNext = data.hasNext;
      this.pagination.hasPrevious = data.hasPrevious;
    },
    changePage(page) {
      if (page >= 1 && page <= this.pagination.totalPages) {
        this.searchCriteria.page = page;
        this.fetchActivities();
      }
    },
  },
};
</script>

<template>
  <div id="activity-search">
    <form @submit.prevent="submitSearch" class="search-form">
      <input v-model="searchCriteria.query" placeholder="Enter Query" class="input-field"/>

      <button type="submit" class="search-button">Search</button>
    </form>

    <div v-if="activities.length">
      <h3> 1-{{ pagination.pageSize }} of {{ pagination.totalHits }} results </h3>

      <table>
        <thead>
        <tr>
          <th>S. No</th>
          <th>Title</th>
          <th>Price</th>
          <th>Rating</th>
          <th>Special Offer</th>
          <th>Supplier Name</th>
          <th>Location</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(activity, index) in activities" :key="activity.id">
          <td> {{ (pagination.currentPage - 1) * 10 + index + 1 }} </td>
          <td> {{ activity.title }}</td>
          <td> {{ activity.price }} {{ activity.currency }}</td>
          <td> {{ activity.rating }}</td>
          <td> {{ activity.specialOffer ? 'Yes' : 'No' }}</td>
          <td> {{ activity.supplierName }}</td>
          <td> {{ activity.location }}</td>
        </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button @click="changePage(pagination.currentPage - 1)" :disabled="!pagination.hasPrevious">Previous</button>
        <span>Page {{ pagination.currentPage }} of {{ pagination.totalPages }}</span>
        <button @click="changePage(pagination.currentPage + 1)" :disabled="!pagination.hasNext">Next</button>
      </div>
    </div>
    <div v-else>
      <p>No activities found.</p>
    </div>


  </div>
</template>

<style>
.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.input-field {
  width: 500px;
  height: 20px;
  padding: 5px;
  font-size: 16px;
}

.search-button {
  width: 100px;
  height: 32px;
  padding: 0 20px;
  font-size: 16px;
}

.pagination {
  margin-top: 20px;
}

.pagination button {
  margin-right: 10px;
}

.pagination span {
  margin-right: 10px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

table, th, td {
  border: 1px solid black;
}

th, td {
  padding: 10px;
  text-align: left;
}
</style>
