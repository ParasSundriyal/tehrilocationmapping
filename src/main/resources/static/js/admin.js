async function acceptOccurrence(occurrenceId) {
    try {
        const response = await fetch(`/api/occurrences/${occurrenceId}/accept`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            // Redirect to emergency response page
            window.location.href = `/emergency-response.html?id=${occurrenceId}`;
        } else {
            throw new Error('Failed to accept occurrence');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to accept occurrence');
    }
} 