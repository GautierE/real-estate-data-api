# real-estate-data-api
Java-based web API hosted on Google Cloud Platform that provides mock real estate data retrieved from a Cassandra database hosted using (DataStax) Astra DB.

<b>API documentation : </b>
<ol>
    <li>
        <p>Route: <code>/properties</code></p>
        <ul>
            <li>Method: GET</li>
            <li>Description: Retrieves all properties.</li>
            <li>Request: Make a GET request to <code>/properties</code>.</li>
            <li>Response: Returns a JSON array containing all properties.</li>
        </ul>
    </li>
    <li>
        <p>Route: <code>/property</code></p>
        <ul>
            <li>Method: POST</li>
            <li>Description: Creates a new property.</li>
            <li>Request: Make a POST request to <code>/property</code> with a JSON payload containing the property
                details.</li>
          <li>Valid body example : { <br/>
        "propertyId": 769, <br/>
        "propertyType": "house", <br/>
        "address": "19 Sundown Terrace", <br/>
        "city": "Pescara", <br/>
        "state": "Abruzzi", <br/>
        "postalCode": 65129, <br/>
        "price": 514654, <br/>
        "bedrooms": 8, <br/>
        "bathrooms": 3, <br/>
        "yearBuilt": 2000 <br/>
    }
          </li>
            <li>Response: Returns the created property as a JSON object.</li>
        </ul>
    </li>
    <li>
        <p>Route: <code>/property?propertyId={id}</code></p>
        <ul>
            <li>Method: GET</li>
            <li>Description: Retrieves a specific property by its ID.</li>
            <li>Request: Make a GET request to <code>/property?propertyId={id}</code>, replacing <code>{id}</code> with
                the ID of the property you want to retrieve.</li>
            <li>Response: Returns the property with the specified ID as a JSON object if found, or an error message if
                not found.</li>
        </ul>
    </li>
    <li>
        <p>Route: <code>/property?propertyId={id}</code></p>
        <ul>
            <li>Method: PUT</li>
            <li>Description: Updates a specific property by its ID.</li>
            <li>Request: Make a PUT request to <code>/property?propertyId={id}</code>, replacing <code>{id}</code> with
                the ID of the property you want to update. Include a JSON payload containing the updated property
                details.</li>
            <li>Response: Returns the updated property as a JSON object if the update is successful, or an error message
                if the update fails.</li>
        </ul>
    </li>
    <li>
        <p>Route: <code>/property?propertyId={id}</code></p>
        <ul>
            <li>Method: DELETE</li>
            <li>Description: Deletes a specific property by its ID.</li>
            <li>Request: Make a DELETE request to <code>/property?propertyId={id}</code>, replacing <code>{id}</code>
                with the ID of the property you want to delete.</li>
            <li>Response: Returns a success message if the deletion is successful, or an error message if the deletion
                fails.</li>
        </ul>
    </li>
</ol>
