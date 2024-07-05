using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;
using Amazon.DynamoDBv2.DocumentModel;
using Amazon.DynamoDBv2.Model;

namespace DynamoCRUD;

public class DynamoDBTables
{
    static async Task Main()
    {
        var client = new AmazonDynamoDBClient();
        DynamoDBContext context = new DynamoDBContext(client);

        await RetriveNote(context);
        await AddNote(context);
        await RetriveNote(context);
        await DisplayFavoriteNotesForUser(context);
    }

    private static async Task<Note> RetriveNote(DynamoDBContext context)
    {
        Note note = await context.LoadAsync<Note>("StudentA", 55);
        if (note != null) {
            Console.WriteLine($"***** The noe reads: {note.NoteString}");
        }
        return note;
    }

    private static async Task AddNote(DynamoDBContext context)
    {
        Note noteToAdd = new Note {
            UserId = "StudentA",
            NoteId = 55,
            NoteString = "This is a sample note.",
            Favorite = "Yes"
        };
        await context.SaveAsync(noteToAdd);
    }

    private static async Task DisplayFavoriteNotesForUser(DynamoDBContext context)
    {
        var results = await context.QueryAsync<Note>("StudentA", new DynamoDBOperationConfig {
            QueryFilter = new List<ScanCondition>() { new ScanCondition("Favorite", ScanOperator.Equal, "Yes") }
        }).GetRemainingAsync();
        Console.WriteLine("***** Query result is ...");
        results.ForEach(Console.WriteLine);
    }

}
