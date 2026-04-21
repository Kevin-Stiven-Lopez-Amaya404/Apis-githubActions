const express = require('express');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());

let tasks = [
  { id: 1, title: 'Tarea 1', description: 'Primera tarea', completed: false }
];

app.get('/', (req, res) => {
  res.json({ message: 'API Node.js funcionando' });
});

app.get('/tasks', (req, res) => {
  res.json(tasks);
});

app.get('/tasks/:id', (req, res) => {
  const task = tasks.find(t => t.id === parseInt(req.params.id));
  if (!task) return res.status(404).json({ message: 'Tarea no encontrada' });
  res.json(task);
});

app.post('/tasks', (req, res) => {
  const newTask = {
    id: tasks.length ? tasks[tasks.length - 1].id + 1 : 1,
    title: req.body.title,
    description: req.body.description,
    completed: req.body.completed || false
  };
  tasks.push(newTask);
  res.status(201).json(newTask);
});

app.put('/tasks/:id', (req, res) => {
  const index = tasks.findIndex(t => t.id === parseInt(req.params.id));
  if (index === -1) return res.status(404).json({ message: 'Tarea no encontrada' });

  tasks[index] = {
    ...tasks[index],
    title: req.body.title ?? tasks[index].title,
    description: req.body.description ?? tasks[index].description,
    completed: req.body.completed ?? tasks[index].completed
  };

  res.json(tasks[index]);
});

app.delete('/tasks/:id', (req, res) => {
  const index = tasks.findIndex(t => t.id === parseInt(req.params.id));
  if (index === -1) return res.status(404).json({ message: 'Tarea no encontrada' });

  const deleted = tasks.splice(index, 1);
  res.json({ message: 'Tarea eliminada', task: deleted[0] });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Servidor corriendo en puerto ${PORT}`);
});