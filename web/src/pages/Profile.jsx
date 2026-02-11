import React from 'react'
import { Link } from 'react-router-dom'

export default function Profile({ user }) {
  return (
    <div style={{padding: '2rem'}}>
      <div style={{marginBottom: '1rem'}}>
        <Link to="/dashboard"><button className="primary-btn">Back</button></Link>
      </div>
      <h2>Profile</h2>
      <div style={{marginTop: '1rem'}}>
        <p><strong>Name:</strong> {user?.name || '—'}</p>
        <p><strong>Email:</strong> {user?.email || '—'}</p>
      </div>
    </div>
  )
}
