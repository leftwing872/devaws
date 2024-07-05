using Amazon.DynamoDBv2.DataModel;

namespace DynamoCRUD;

[DynamoDBTable("Notes")]
    public class Note
    {
        // Partition key mapping.
        [DynamoDBHashKey] //Partition key
        public string UserId { get; set; }
        
        [DynamoDBRangeKey] //Sort key
        public int NoteId { get; set; }
        
        [DynamoDBProperty("Note")]
        public string NoteString { get; set; }

        [DynamoDBProperty]
        public string Favorite { get; set; }

    public override string ToString()
    {
        return $"Note [ {UserId} | {NoteId} | {NoteString} | {Favorite} ]";
    }
}