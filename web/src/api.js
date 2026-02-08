const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export async function api(path, options = {}) {
  const token = localStorage.getItem('token')
  const headers = Object.assign({ 'Content-Type': 'application/json' }, options.headers || {})
  if (token) headers['Authorization'] = `Bearer ${token}`
  const res = await fetch(`${API_URL}${path}`, Object.assign({}, options, { headers }))
  if (!res.ok) {
    const text = await res.text().catch(() => '')
    const err = new Error(res.status + ' ' + res.statusText)
    err.status = res.status
    err.body = text
    throw err
  }
  return res.status === 204 ? null : res.json().catch(() => null)
}

export default api
