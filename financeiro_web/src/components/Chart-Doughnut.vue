<script>
/*import { Pie } from 'vue-chartjs'

export default {
  extends: Pie,
  data: () => ({
    chartdata: {
      labels: ['Receitas', 'Despesas', 'Caixa Mensal', 'Patrimônio Acumulado'],
      datasets: [
        {
          label: 'Balanço geral',
          backgroundColor: '#f87979',
          data: [22.000, 15.000, 1.000, 5.000, ]
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false
    }
  }),

  mounted () {
    this.renderChart(this.chartdata, this.options)
  }
}*/

import { Doughnut } from 'vue-chartjs'

export default {
    extends: Doughnut,
    props: {
        chartdata: {
            type: Object,
            default: null
        },
        options: {
            type: Object,
            default: null
        }
    },
    
    mounted () {
        this.renderChart(this.chartdata, this.options)
    }
}
</script>